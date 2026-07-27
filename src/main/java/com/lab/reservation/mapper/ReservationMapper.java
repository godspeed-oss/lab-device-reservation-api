package com.lab.reservation.mapper;

import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ReservationMapper {
    @Select("""
            <script>
            SELECT
                r.id,
                r.device_id AS deviceId,
                d.name AS deviceName,
                r.user_id AS userId,
                r.user_name AS userName,
                r.reservation_date AS reservationDate,
                r.start_time AS startTime,
                r.end_time AS endTime
            FROM reservation r
            LEFT JOIN device d ON r.device_id = d.id
            WHERE 1 = 1
            <if test="deviceId != null">
                AND r.device_id = #{deviceId}
            </if>
            <if test="reservationDate != null">
                AND r.reservation_date = #{reservationDate}
            </if>
            <if test="userId != null">
                AND r.user_id = #{userId}
            </if>
            ORDER BY r.id DESC
            LIMIT #{size} OFFSET #{offset}
            </script>
            """)
    List<ReservationResponse> search(
            @Param("deviceId") Integer deviceId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("userId") Integer userId,
            @Param("offset") int offset,
            @Param("size") int size
    );

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM reservation r
            WHERE 1 = 1
            <if test="deviceId != null">
                AND r.device_id = #{deviceId}
            </if>
            <if test="reservationDate != null">
                AND r.reservation_date = #{reservationDate}
            </if>
            <if test="userId != null">
                AND r.user_id = #{userId}
            </if>
            </script>
            """)
    long count(
            @Param("deviceId") Integer deviceId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("userId") Integer userId
    );

    @Select("""
            SELECT
                r.id,
                r.device_id AS deviceId,
                d.name AS deviceName,
                r.user_id AS userId,
                r.user_name AS userName,
                r.reservation_date AS reservationDate,
                r.start_time AS startTime,
                r.end_time AS endTime
            FROM reservation r
            LEFT JOIN device d ON r.device_id = d.id
            WHERE r.id = #{id}
            """)
    ReservationResponse findById(Integer id);

    @Select("""
            SELECT
                id,
                device_id AS deviceId,
                user_id AS userId,
                user_name AS userName,
                reservation_date AS reservationDate,
                start_time AS startTime,
                end_time AS endTime
            FROM reservation
            WHERE id = #{id}
            """)
    Reservation findEntityById(Integer id);

    @Insert("""
            INSERT INTO reservation
            (device_id, user_id, user_name, reservation_date, start_time, end_time)
            VALUES
            (#{deviceId}, #{userId}, #{userName}, #{reservationDate}, #{startTime}, #{endTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reservation reservation);

    @Delete("DELETE FROM reservation WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("""
            SELECT COUNT(*)
            FROM reservation
            WHERE device_id = #{deviceId}
              AND reservation_date = #{reservationDate}
              AND id != #{excludeReservationId}
              AND start_time < #{endTime}
              AND end_time > #{startTime}
            """)
    int countTimeConflict(
            @Param("deviceId") Integer deviceId,
            @Param("reservationDate") LocalDate reservationDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeReservationId") Integer excludeReservationId
    );

@Select("SELECT COUNT(*) FROM reservation WHERE device_id = #{deviceId}")
int countByDeviceId(Integer deviceId);
}