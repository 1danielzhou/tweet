package com.daniel.ltc20.model;

import com.daniel.ltc20.domain.TweetBaseContent;
import com.daniel.ltc20.domain.TweetRelationMention;
import com.daniel.ltc20.domain.TweetRelationPostView;
import com.daniel.ltc20.domain.TweetRelationTopic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetContent {
    private TweetBaseContent tweetBaseContent;
    private List<TweetRelationTopic> tweetTopics;
    private List<TweetRelationMention> tweetMentions;
    private List<TweetRelationPostView> postViews;
}