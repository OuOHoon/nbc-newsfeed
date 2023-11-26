package com.sparta.newsfeed.like;

import com.sparta.newsfeed.common.exception.like.SelfLikeException;
import com.sparta.newsfeed.common.exception.post.NotFoundPostException;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.post.PostRepository;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public Integer likePost(Long postId, User user) {
        Post post = findPost(postId);
        checkUser(post, user);
        likesRepository.save(new Likes(post, user));
        return post.getLikesCount();
    }

    public Integer unlikePost(Long postId, User user) {
        Post post = findPost(postId);
        Likes like = likesRepository.findByPostIdAndUserId(postId, user.getId());
        likesRepository.delete(like);
        return post.getLikesCount();
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(NotFoundPostException::new);
    }

    private void checkUser(Post post, User user) {
        if (post.getUser().getId().equals(user.getId())) {
            throw new SelfLikeException();
        }
    }
}
