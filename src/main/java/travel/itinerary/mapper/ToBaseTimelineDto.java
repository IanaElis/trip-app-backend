package travel.itinerary.mapper;

import org.mapstruct.Mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", source = "id")
@Mapping(target = "tripId", source = "trip.id")
@Mapping(target = "startDateTime", source = "startDateTime")
@Mapping(target = "endDateTime", source = "endDateTime")
@Mapping(target = "notes", source = "notes")
@Target(ElementType.METHOD)
public @interface ToBaseTimelineDto {
}
