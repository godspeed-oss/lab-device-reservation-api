package com.lab.reservation.mapper;

import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {
    @Select("""
            <script>
            SELECT
                r.id,
                r.device_id,
                d.name AS device_name,
                d.type AS device_type,
                r.user_name,
                r.reservation_date,
                r.start_time,
                r.end_time
            FROM reservation r
            JOIN device d ON r.device_id = d.id
            WHERE 1 = 1
            <if test="deviceId != null">
                AND r.device_id = #{deviceId}
            </if>
            <if test="date != null">
                AND r.reservation_date = #{date}
            </if>
            ORDER BY r.id
            LIMIT #{size} OFFSET #{offset}
            </script>
            """)
    List<ReservationResponse> searchWithDevice(@Param("deviceId") Integer deviceId,
                                               @Param("date") LocalDate date,
                                               @Param("offset") int offset,
                                               @Param("size") int size);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM reservation r
            JOIN device d ON r.device_id = d.id
            WHERE 1 = 1
            <if test="deviceId != null">
                AND r.device_id = #{deviceId}
            </if>
            <if test="date != null">
                AND r.reservation_date = #{date}
            </if>
            </script>
            """)
    long countWithDevice(@Param("deviceId") Integer deviceId, @Param("date") LocalDate date);

    @Select("""
            SELECT
                r.id,
                r.device_id,
                d.name AS device_name,
                d.type AS device_type,
                r.user_name,
                r.reservation_date,
                r.start_time,
                r.end_time
            FROM reservation r
            JOIN device d ON r.device_id = d.id
            WHERE r.id = #{id}
            """)
    ReservationResponse findByIdWithDevice(Integer id);

    @Select("SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE id = #{id}")
    Reservation findById(Integer id);

    @Select("SELECT COUNT(*) FROM reservation WHERE device_id = #{deviceId}")
    long countByDeviceId(Integer deviceId);

    @Select("SELECT COUNT(*) FROM reservation WHERE device_id = #{deviceId} AND reservation_date = #{reservationDate} AND start_time < #{endTime} AND end_time > #{startTime}")
    int countTimeConflict(Reservation reservation);

    @Insert("INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (#{deviceId}, #{userName}, #{reservationDate}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reservation reservation);

    @Delete("DELETE FROM reservation WHERE id = #{id}")
    int deleteById(Integer id);
}