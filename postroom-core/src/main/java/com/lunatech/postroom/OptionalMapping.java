package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.*;

class OptionalMapping<T> implements Mapping<Optional<T>> {

    private final Mapping<T> underlying;
    private final List<Constraint<Optional<T>>> constraints;

    public OptionalMapping(Mapping<T> underlying, List<Constraint<Optional<T>>> constraints) {
        this.underlying = underlying;
        this.constraints = constraints;
    }

    @Override
    public Either<List<FormError>, Optional<T>> bind(Map<String, String> data) {
        String prefix = underlying.key().split("\\.", 2)[0];
        if ((data.containsKey(prefix) && !data.get(prefix).isEmpty())
                || data.entrySet().stream().anyMatch(entry ->
                entry.getKey().startsWith(prefix + ".") && !entry.getValue().isEmpty())) {
            return underlying.bind(data).map(Optional::of);
        } else {
            return Either.right(Optional.empty());
        }
    }

    @Override
    public Map<String, String> unbind(Optional<T> value) {
        return value.map(underlying::unbind).orElse(Collections.emptyMap());
    }

    @Override
    public String key() {
        return underlying.key();
    }

    @Override
    public Mapping<Optional<T>> withPrefix(String prefix) {
        return new OptionalMapping<>(underlying.withPrefix(prefix), constraints);
    }

    @Override
    public Mapping<Optional<T>> verifying(Collection<Constraint<Optional<T>>> constraints) {
        List<Constraint<Optional<T>>> allConstraints = new ArrayList<>(this.constraints.size() + constraints.size());
        allConstraints.addAll(this.constraints);
        allConstraints.addAll(constraints);
        return new OptionalMapping<>(underlying, allConstraints);
    }

}
