package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.View;

@Repository
public interface ViewedPostRepository extends JpaRepository<ViewedPostHistory,Long> {
}
