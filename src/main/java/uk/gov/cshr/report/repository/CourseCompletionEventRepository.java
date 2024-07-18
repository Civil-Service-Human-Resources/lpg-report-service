package uk.gov.cshr.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;

import java.time.LocalDateTime;
import java.util.List;

public interface CourseCompletionEventRepository extends JpaRepository<CourseCompletionEvent, Long> {

    @Query("""
            select date_trunc_tz(:delimiter, cce.eventTimestamp, :timezone) as dateBin, cce.courseId as courseId, count(cce) as total
            from CourseCompletionEvent cce
            where cce.eventTimestamp >= :from and cce.eventTimestamp <= :to
            and (:courseIds is null or cce.courseId in :courseIds)
            and (:organisationIds is null or cce.organisationId in :organisationIds)
            and (:gradeIds is null or cce.gradeId in :gradeIds)
            and (:professionIds is null or cce.professionId in :professionIds)
            group by 1, 2
            order by 1 asc, 2 asc""")
    List<CourseCompletionAggregation> getCompletionsAggregationByCourse(@Param("delimiter") String delimiter,
                                                                        @Param("from") LocalDateTime from,
                                                                        @Param("to") LocalDateTime to,
                                                                        @Param("timezone") String timezone,
                                                                        @Param("courseIds") List<String> courseIds,
                                                                        @Param("organisationIds") List<Integer> organisationIds,
                                                                        @Param("gradeIds") List<Integer> gradeIds,
                                                                        @Param("professionIds") List<Integer> professionIds);


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
    UPDATE CourseCompletionEvent cce
    SET cce.userId = NULL, cce.userEmail = NULL
    WHERE cce.userId in :uids
""")
    int removeUserDetails(List<String> uids);
}
