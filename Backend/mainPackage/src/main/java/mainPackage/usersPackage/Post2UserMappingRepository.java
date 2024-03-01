package mainPackage.usersPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Junhyung Shim
 * */
@Repository
public interface Post2UserMappingRepository extends JpaRepository<Post2UserMapping,Long> {
    public Post2UserMapping findPost2UserMappingByPidAndUid(int pid, String uid);
}
