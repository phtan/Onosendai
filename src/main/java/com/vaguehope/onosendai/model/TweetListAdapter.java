package com.vaguehope.onosendai.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vaguehope.onosendai.R;
import com.vaguehope.onosendai.images.ImageLoadRequest;
import com.vaguehope.onosendai.images.ImageLoader;

public class TweetListAdapter extends BaseAdapter {

	private final LayoutInflater layoutInflater;
	private final ImageLoader imageLoader;

	private TweetList listData;

	public TweetListAdapter (final Context context, final ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void setInputData (final TweetList data) {
		this.listData = data;
		notifyDataSetChanged();
	}

	public TweetList getInputData () {
		return this.listData;
	}

	@Override
	public int getCount () {
		return this.listData == null ? 0 : this.listData.count();
	}

	@Override
	public Object getItem (final int position) {
		return getTweet(position);
	}

	@Override
	public long getItemId (final int position) {
		Tweet tweet = getTweet(position);
		if (tweet == null) return -1;
		return tweet.getUid();
	}

	public Tweet getTweet (final int position) {
		if (this.listData == null) return null;
		if (position >= this.listData.count()) return null;
		return this.listData.getTweet(position);
	}

	@Override
	public View getView (final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		RowView rowView;
		if (view == null) {
			view = this.layoutInflater.inflate(R.layout.tweetlistrow, null);
			rowView = new RowView(
					(ImageView) view.findViewById(R.id.imgMain),
					(TextView) view.findViewById(R.id.txtTweet),
					(TextView) view.findViewById(R.id.txtName)
					);
			view.setTag(rowView);
		}
		else {
			rowView = (RowView) view.getTag();
		}

		Tweet item = this.listData.getTweet(position);
		rowView.getTweet().setText(item.getBody());
		rowView.getName().setText(item.getUsername() != null ? item.getUsername() : item.getFullname());

		String avatarUrl = item.getAvatarUrl();
		if (avatarUrl != null) {
			this.imageLoader.loadImage(new ImageLoadRequest(avatarUrl, rowView.getAvatar()));
		}
		else {
			rowView.getAvatar().setImageResource(R.drawable.question_blue);
		}

		return view;
	}

	private static class RowView {

		private final ImageView avatar;
		private final TextView tweet;
		private final TextView name;

		public RowView (final ImageView avatar, final TextView tweet, final TextView name) {
			this.avatar = avatar;
			this.tweet = tweet;
			this.name = name;
		}

		public ImageView getAvatar () {
			return this.avatar;
		}

		public TextView getTweet () {
			return this.tweet;
		}

		public TextView getName () {
			return this.name;
		}

	}
	
	public void sortByThread(){
	    TweetTreeNode root = new TweetTreeNode();
	    ArrayList<Tweet> result = new ArrayList<Tweet>();
	    for(Tweet tweet: listData.getTweets()) {
	        Meta inReplyTo = tweet.getFirstMetaOfType(MetaType.INREPLYTO);
	        
	        if(inReplyTo == null) {
	            root.add(tweet);
	        } else {
	            TweetTreeNode replyedTweet = root.findBySid(inReplyTo.getData());
	            replyedTweet.add(tweet);
	        }
	    }
	    Queue<TweetTreeNode> queue = new ArrayDeque<TweetTreeNode>();
	    queue.offer(root);
	    while(!queue.isEmpty()) {
	        TweetTreeNode node = queue.poll();
	        
	        if(node != root) {
	            result.add(node.getTweet());
	        }
	        
            if (node == root) {
                Collections.sort(node.getChildren(), new Comparator<TweetTreeNode>() {
                    @Override
                    public int compare(TweetTreeNode lhs, TweetTreeNode rhs) {
                        long t1 = lhs.getTweet().getTime();
                        long t2 = rhs.getTweet().getTime();

                        if(t1 == t2) {
                            return 0;
                        } else if(t1 > t2) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {
                Collections.sort(node.getChildren(), new Comparator<TweetTreeNode>() {
                    @Override
                    public int compare(TweetTreeNode lhs, TweetTreeNode rhs) {
                        long t1 = lhs.getTweet().getTime();
                        long t2 = rhs.getTweet().getTime();

                        if(t1 == t2) {
                            return 0;
                        } else if(t1 > t2) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
            }
	        
	        for(TweetTreeNode child: node.getChildren()) {
	            queue.offer(child);
	        }
	    }
	    this.listData = new TweetList(result);
		notifyDataSetChanged();
	}

    public void sortByTimeline() {
	    ArrayList<Tweet> result = new ArrayList<Tweet>(listData.getTweets());
        Collections.sort(result,  new Comparator<Tweet>() {
	            @Override
	            public int compare(Tweet o1, Tweet o2) {
	                long t1 = o1.getTime();
                    long t2 = o2.getTime();
	                
	                if(t1 > t2)
	                    return -1;
                    else if(t1 == t2)
	                    return 0;
	                else {
	                    return 1;
	                }
	            }
            });
        this.listData = new TweetList(result);
		notifyDataSetChanged();
    }

	private static class TweetTreeNode {
	    
	    private Tweet tweet;
	    private ArrayList<TweetTreeNode> children;
	    
	    public TweetTreeNode() {
	        this(null);
	    }
	    
	    public TweetTreeNode(Tweet tweet) {
	        this.tweet = tweet;
	        this.children = new ArrayList<TweetTreeNode>();
	    }
	    
	    public Tweet getTweet() {
	        return this.tweet;
	    }
	    
	    public ArrayList<TweetTreeNode> getChildren(){
	        return this.children;
	    }
	    
	    public void add(Tweet tweet) {
	        this.children.add(new TweetTreeNode(tweet));
	    }
	    
	    public TweetTreeNode findBySid(String sid) {
	        if(this.tweet.getSid().equals(sid)) {
	            return this;
	        }
	        
	        Queue<TweetTreeNode> queue = new ArrayDeque<TweetTreeNode>();	        
	        queue.offer(this);
	        
	        while(!queue.isEmpty()) {
	            TweetTreeNode node = queue.poll();
	            
	            if(node.getTweet() != null && node.getTweet().getSid().equals(sid)) {
	                return node;
	            }
	            
	            for(TweetTreeNode child: node.getChildren()) {
	                queue.offer(child);
	            }
	        }
	        
	        return null;
	    }
	}
}
