package com.college.tourism.entity.TouristPlace;

import com.college.tourism.audit.Auditable;
import com.college.tourism.helpers.StringListJsonConverter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Data
@Table(name = "tourist_places")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EntityListeners(AuditingEntityListener.class)
public class TouristPlace extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(columnDefinition = "JSON")
    @Convert(converter = StringListJsonConverter.class)
    private List<String> imageUrl;

}
