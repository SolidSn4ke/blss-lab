package com.example.blsslab.specs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.blsslab.exception.BadRequestBodyException;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class CustomSpecification {

    public static <T> Specification<T> buildFromFilters(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) {
            return null;
        }

        String[] filters = searchQuery.split(";");
        List<Specification<T>> specs = new ArrayList<>();

        for (String filter : filters) {
            Specification<T> spec = parseFilter(filter.trim());
            if (spec != null) {
                specs.add(spec);
            }
        }

        return Specification.allOf(specs);
    }

    private static <T> Specification<T> parseFilter(String filter) {
        if (filter.contains(">=")) {
            String[] parts = filter.split(">=");
            return ge(parts[0].trim(), parseNumber(parts[1].trim()));
        } else if (filter.contains("<=")) {
            String[] parts = filter.split("<=");
            return le(parts[0].trim(), parseNumber(parts[1].trim()));
        } else if (filter.contains(">")) {
            String[] parts = filter.split(">");
            return gt(parts[0].trim(), parseNumber(parts[1].trim()));
        } else if (filter.contains("<")) {
            String[] parts = filter.split("<");
            return lt(parts[0].trim(), parseNumber(parts[1].trim()));
        } else if (filter.contains("~")) {
            String[] parts = filter.split("~");
            return like(parts[0].trim(), parts[1].trim());
        } else if (filter.contains("=")) {
            String[] parts = filter.split("=");
            return eq(parts[0].trim(), parseValue(parts[1].trim()));
        }

        return null;
    }

    private static Object parseValue(String value) {
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private static Number parseNumber(String value) {
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
            throw new BadRequestBodyException(String.format("Unable to parse Number from '%s'", value));
        }
    }

    private static <T> Path<Object> getPath(Root<T> root, String fieldPath) {
        String[] fields = fieldPath.split("[.]");
        Path<Object> path = root.get(fields[0]);

        for (int i = 1; i < fields.length; i++) {
            path = path.get(fields[i]);
        }

        return path;
    }

    public static <T, C> Specification<C> eq(String fieldName, T value) {
        return (root, query, cb) -> cb.equal(getPath(root, fieldName), value);
    }

    public static <T extends Number, C> Specification<C> gt(String fieldName, T value) {
        return (root, query, cb) -> cb.gt(getPath(root, fieldName).as((Class<T>) value.getClass()), value);
    }

    public static <T extends Number, C> Specification<C> lt(String fieldName, T value) {
        return (root, query, cb) -> cb.lt(getPath(root, fieldName).as((Class<T>) value.getClass()), value);
    }

    public static <T extends Number, C> Specification<C> ge(String fieldName, T value) {
        return (root, query, cb) -> cb.ge(getPath(root, fieldName).as((Class<T>) value.getClass()), value);
    }

    public static <T extends Number, C> Specification<C> le(String fieldName, T value) {
        return (root, query, cb) -> cb.le(getPath(root, fieldName).as((Class<T>) value.getClass()), value);
    }

    public static <C> Specification<C> like(String fieldName, String pattern) {
        return (root, query, cb) -> cb.like(
                cb.lower(getPath(root, fieldName).as(String.class)),
                "%" + pattern.toLowerCase() + "%");
    }
}
