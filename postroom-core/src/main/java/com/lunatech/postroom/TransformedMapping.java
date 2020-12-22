package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransformedMapping<T, U> implements Mapping<U> {

    private final Mapping<T> original;
    private final Function<T, ValidationResult<U>> map;
    private final Function<U, T> contramap;
    private final List<Constraint<U>> constraints;

    TransformedMapping(Mapping<T> original, Function<T, ValidationResult<U>> map, Function<U, T> contramap, List<Constraint<U>> constraints) {
        this.original = original;
        this.map = map;
        this.contramap = contramap;
        this.constraints = constraints;
    }

    @Override
    public Either<List<FormError>, U> bind(Map<String, String> data) {
        return original.bind(data).flatMap(t -> map.apply(t).fold(
                errors -> Either.left(errors.stream().map(error ->
                        new FormError(key(), error)).collect(Collectors.toList())),
                boundValue -> Constraints.applyConstraints(constraints, boundValue)
                        .mapLeft(errors -> errors.stream().map(error ->  new FormError(key(), error)).collect(
                                Collectors.toList()))));
    }

    @Override
    public Map<String, String> unbind(U value) {
        return original.unbind(contramap.apply(value));
    }

    @Override
    public String key() {
        return original.key();
    }

    @Override
    public Mapping<U> withPrefix(String prefix) {
        return new TransformedMapping<>(original.withPrefix(prefix), map, contramap, constraints);
    }

    @Override
    public Mapping<U> verifying(Collection<Constraint<U>> constraints) {
        List<Constraint<U>> allConstraints = new ArrayList<>(this.constraints.size() + constraints.size());
        allConstraints.addAll(this.constraints);
        allConstraints.addAll(constraints);
        return new TransformedMapping<>(original, map, contramap, allConstraints);
    }

}
