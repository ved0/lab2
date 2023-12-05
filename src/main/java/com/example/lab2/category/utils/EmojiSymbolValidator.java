package com.example.lab2.category.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.IntPredicate;

public class EmojiSymbolValidator implements ConstraintValidator<EmojiSymbol, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        IntPredicate predicate1 = Character::isEmoji;
        IntPredicate predicate2 = Character::isEmojiComponent;
        IntPredicate combinedPredicate = predicate1.or(predicate2);
        return s.codePoints().allMatch(combinedPredicate);
    }
}
