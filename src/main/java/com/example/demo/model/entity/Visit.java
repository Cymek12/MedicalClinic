package com.example.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private Patient patient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient))
            return false;
        Visit other = (Visit) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean isVisitDataNull() {
        return Objects.isNull(this.getStartDateTime()) ||
                Objects.isNull(this.getEndDateTime());
    }

    public boolean isDateTimeBeforeNow() {
        return this.getStartDateTime().isBefore(LocalDateTime.now()) || this.getEndDateTime().isBefore(LocalDateTime.now());
    }

    public boolean isTimeFormatCorrect() {
        return this.getStartDateTime().getMinute() % 15 == 0 || this.getEndDateTime().getMinute() % 15 == 0;
    }

    public boolean isEndTimeAfterStartTime() {
        return this.getEndDateTime().isAfter(this.getStartDateTime());
    }

    public boolean isVisitOverlap(Visit visit) {
        return visit.getStartDateTime().isBefore(this.getEndDateTime()) &&
                visit.getEndDateTime().isAfter(this.getStartDateTime());
    }

    public boolean isVisitViolatesBreak(Visit visit) {
        return  !visit.getStartDateTime().isAfter(this.getEndDateTime().plusMinutes(14)) ||
                !visit.getEndDateTime().isBefore(this.getStartDateTime().minusMinutes(14));
    }
}

