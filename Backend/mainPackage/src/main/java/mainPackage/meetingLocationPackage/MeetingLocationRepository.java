package mainPackage.meetingLocationPackage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingLocationRepository extends JpaRepository<MeetingLocation,Long> {
    MeetingLocation findMeetingLocationById(int id);
}
