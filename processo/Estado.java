package processo;

public enum Estado {
    EXECUTANDO("EXECUTANDO"),
    PRONTO("PRONTO"),
    BLOQUEADO("BLOQUEADO");
    private String value;

    Estado(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
