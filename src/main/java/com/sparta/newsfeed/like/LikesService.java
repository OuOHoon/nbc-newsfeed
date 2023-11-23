package com.sparta.newsfeed.like;

import com.sparta.newsfeed.common.exception.NotFoundPostException;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.post.PostRepository;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public Boolean likePost(Long postId, User user) {
        Optional<Likes> like = likesRepository.findByPostIdAndUserId(postId, user.getId());

        if(like.isEmpty()){
            Post post = findPost(postId);
            checkUser(post, user);
            likesRepository.save(new Likes(post, user));
            return true;
        } else{
            likesRepository.delete(like.get());
            return false;
        }
    }

    public int countLikes(Long postId) {
        Post post = findPost(postId);
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
