package com.example.pets.repos;

import com.example.pets.domain.Pet;
import com.example.pets.domain.User;
import com.example.pets.domain.dto.PetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PetRepo extends CrudRepository<Pet, Long> {
    @Query("select new com.example.pets.domain.dto.PetDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Pet m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<PetDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new com.example.pets.domain.dto.PetDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Pet m left join m.likes ml " +
            "group by m")
    Page<PetDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new com.example.pets.domain.dto.PetDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Pet m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<PetDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
