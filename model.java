package modelodedatos;

public class model {
    //Declaramos atributos
    private String id, group, lecture, activity;

    public model() {
    }

    public model(String id, String group, String lecture, String activity) {
        this.id = id;
        this.group = group;
        this.lecture = lecture;
        this.activity = activity;
    }

    public model(String group, String lecture, String activity) {
        this.group = group;
        this.lecture = lecture;
        this.activity = activity;
    }

    //Retorna Tipo String
    public String getId() {
        return id;
    }
    //Guarda Cosas
    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

}
