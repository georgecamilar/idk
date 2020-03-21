package model;


//This is the participant-proba table
public class Nota implements HasId<Integer> {
    private Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Nota(Integer id, Integer participantId, Integer probaId, Double nota) {
        this.id = id;
        this.participantId = participantId;
        this.probaId = probaId;
        this.nota = nota;
    }

    private Integer participantId;
    private Integer probaId;
    private Double nota;

    public Nota(Integer participantId, Integer probaId, Double nota) {
        this.participantId = participantId;
        this.probaId = probaId;
        this.nota = nota;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getProbaId() {
        return probaId;
    }

    public void setProbaId(Integer probaId) {
        this.probaId = probaId;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

}
