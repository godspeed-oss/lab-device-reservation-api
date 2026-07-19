package com.lab.reservation.mapper;

import com.lab.reservation.entity.Device;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DeviceMapper {
    @Select("""
            <script>
            SELECT id, name, type, status
            FROM device
            WHERE 1 = 1
            <if test="keyword != null and keyword != ''">
                AND (name LIKE CONCAT('%', #{keyword}, '%') OR type LIKE CONCAT('%', #{keyword}, '%'))
            </if>
            <if test="status != null and status != ''">
                AND status = #{status}
            </if>
            ORDER BY id
            </script>
            """)
    List<Device> search(@Param("keyword") String keyword, @Param("status") String status);

    @Select("SELECT id, name, type, status FROM device WHERE id = #{id}")
    Device findById(Integer id);

    @Insert("INSERT INTO device (name, type, status) VALUES (#{name}, #{type}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Device device);

    @Update("UPDATE device SET name = #{name}, type = #{type}, status = #{status} WHERE id = #{id}")
    int update(Device device);

    @Delete("DELETE FROM device WHERE id = #{id}")
    int deleteById(Integer id);
}