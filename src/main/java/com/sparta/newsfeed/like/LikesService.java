package com.sparta.newsfeed.like;


import com.sparta.newsfeed.comment.Comment;
import com.sparta.newsfeed.comment.CommentRepository;
import com.sparta.newsfeed.common.exception.comment.NotFoundCommentException;
import com.sparta.newsfeed.common.exception.like.AlreadyLikeException;
import com.sparta.newsfeed.common.exception.like.NotFoundLikeException;
import com.sparta.newsfeed.common.exception.like.SelfLikeException;
import com.sparta.newsfeed.common.exception.post.NotFoundPostException;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.post.PostRepository;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostLikesRepository postLikesRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public Integer likePost(Long postId, User user) {
        Post post = findPost(postId);
        checkUser(post, user);
        isAlreadyLikePost(user, postId);
        postLikesRepository.save(new PostLikes(post, user));
        return post.increaseCount();
    }

    @Transactional
    public Integer unlikePost(Long postId, User user) {
        Post post = findPost(postId);
        PostLikes like = postLikesRepository.findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(NotFoundLikeException::new);
        postLikesRepository.delete(like);
        return post.decreaseCount();
    }

    @Transactional
    public Integer likeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUser(comment, user);
        isAlreadyLikeComment(user, commentId);
        commentLikesRepository.save(new CommentLikes(comment, user));
        return comment.increaseCount();
    }

    @Transactional
    public Integer unlikeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        CommentLikes like = commentLikesRepository.findByCommentIdAndUserId(commentId, user.getId())
                .orElseThrow(NotFoundLikeException::new);
        commentLikesRepository.delete(like);
        return comment.decreaseCount();
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

    private void isAlreadyLikePost(User user, Long postId) {
        Optional<PostLikes> likes = postLikesRepository.findByPostIdAndUserId(postId, user.getId());
        if (likes.isPresent()){
            throw new AlreadyLikeException();
        }
    }

    private void isAlreadyLikeComment(User user, Long commentId) {
        Optional<CommentLikes> likes = commentLikesRepository.findByCommentIdAndUserId(commentId, user.getId());
        if (likes.isPresent()){
            throw new AlreadyLikeException();
        }
    }
}
