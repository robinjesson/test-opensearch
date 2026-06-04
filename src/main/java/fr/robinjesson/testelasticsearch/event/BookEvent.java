package fr.robinjesson.testelasticsearch.event;

public record BookEvent(Long bookId, ActionType action) {
    public enum ActionType {
        SAVE, UPDATE, DELETE
    }
}