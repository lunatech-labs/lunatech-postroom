package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TransformedMapping<T, U> implements Mapping<U> {

    private final Mapping<T> original;
    private final Function<T, U> map;
    private final Function<U, T> contramap;

    TransformedMapping(Mapping<T> original, Function<T, U> map, Function<U, T> contramap) {
        this.original = original;
        this.map = map;
        this.contramap = contramap;
    }

    @Override
    public Either<List<FormError>, U> bind(Map<String, String> data) {
        return original.bind(data).map(map);
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
        return new TransformedMapping<>(original.withPrefix(prefix), map, contramap);
    }

    @Override
    public Mapping<U> verifying(Collection<Constraint<U>> constraints) {
        ArrayList<Constraint<T>> mappedConstraints = new ArrayList<>(constraints.size());
        for(Constraint<U> c : constraints) {
            mappedConstraints.add(t -> c.validate(map.apply(t)));
        }
        return new TransformedMapping<>(original.verifying(mappedConstraints), map, contramap);
    }

}
