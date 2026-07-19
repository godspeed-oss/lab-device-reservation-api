package com.lab.reservation.mapper;

import com.lab.reservation.dto.ReservationResponse;
import com.lab.reservation.entity.Reservation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReservationMapper {
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
            ORDER BY r.id
            """)
    List<ReservationResponse> findAllWithDevice();

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

    @Select("SELECT COUNT(*) FROM reservation WHERE device_id = #{deviceId} AND reservation_date = #{reservationDate} AND start_time < #{endTime} AND end_time > #{startTime}")
    int countTimeConflict(Reservation reservation);

    @Insert("INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (#{deviceId}, #{userName}, #{reservationDate}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reservation reservation);

    @Delete("DELETE FROM reservation WHERE id = #{id}")
    int deleteById(Integer id);
}