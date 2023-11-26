package com.sparta.newsfeed.like;

import com.sparta.newsfeed.comment.Comment;
import com.sparta.newsfeed.comment.CommentRepository;
import com.sparta.newsfeed.common.exception.comment.NotFoundCommentException;
import com.sparta.newsfeed.common.exception.like.SelfLikeException;
import com.sparta.newsfeed.common.exception.post.NotFoundPostException;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.post.PostRepository;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public Integer likePost(Long postId, User user) {
        Post post = findPost(postId);
        checkUser(post, user);
        likesRepository.save(new Likes(post, user));
        return post.countLikes();
    }

    @Transactional
    public Integer unlikePost(Long postId, User user) {
        Post post = findPost(postId);
        Likes like = likesRepository.findByPostIdAndUserId(postId, user.getId());
        likesRepository.delete(like);
        return post.countLikes();
    }

    @Transactional
    public Integer likeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUser(comment, user);
        likesRepository.save(new Likes(comment, user));
        return comment.countLikes();
    }

    @Transactional
    public Integer unlikeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        Likes like = likesRepository.findByCommentIdAndUserId(commentId, user.getId());
        likesRepository.delete(like);
        return comment.countLikes();
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(NotFoundPostException::new);
    }

    private Comment findComment(Long id){
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }

    private void checkUser(Post post, User user) {
        if (post.getUser().getId().equals(user.getId())) {
            throw new SelfLikeException();
        }
    }

    private void checkUser(Comment comment, User user){
        if (comment.getUser().getId().equals(user.getId())) {
            throw new SelfLikeException();
        }
    }
}
