package eventa.repository;

import eventa.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    List<Post> findAll();

//    List<Post> findByUserId(long user_id);
}
