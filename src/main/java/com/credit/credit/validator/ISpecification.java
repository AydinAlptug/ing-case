package com.credit.credit.validator;

public interface ISpecification<T> {

	boolean isSatisfiedBy(T t);

	default ISpecification<T> and(ISpecification<T> other) {
		return t -> this.isSatisfiedBy(t) && other.isSatisfiedBy(t);
	}

	default ISpecification<T> or(ISpecification<T> other) {
		return t -> this.isSatisfiedBy(t) || other.isSatisfiedBy(t);
	}

	default ISpecification<T> not() {
		return t -> !this.isSatisfiedBy(t);
	}
}
