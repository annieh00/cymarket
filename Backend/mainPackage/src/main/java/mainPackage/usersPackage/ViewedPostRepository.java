package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.View;
import java.util.List;

@Repository
public interface ViewedPostRepository extends JpaRepository<ViewedPostHistory,Long> {
    List<ViewedPostHistory> findAllByUser(GeneralUser user);

    ViewedPostHistory findViewedPostById(int id);
}
