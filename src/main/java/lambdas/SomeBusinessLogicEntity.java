package lambdas;

import java.util.List;

public class SomeBusinessLogicEntity {

    private int someParameter;
    private List<Integer> someProperties;
    private String someName;

    public SomeBusinessLogicEntity(int someParameter, String someName) {
        this.someParameter = someParameter;
        this.someName = someName;
    }

    public SomeBusinessLogicEntity(int someParameter, List<Integer> someProperties, String someName) {
        this.someParameter = someParameter;
        this.someProperties = someProperties;
        this.someName = someName;
    }

    public int getSomeParameter() {
        return someParameter;
    }

    public void setSomeParameter(int someParameter) {
        this.someParameter = someParameter;
    }

    public List<Integer> getSomeProperties() {
        return someProperties;
    }

    public void setSomeProperties(List<Integer> someProperties) {
        this.someProperties = someProperties;
    }

    public String getSomeName() {
        return someName;
    }

    public void setSomeName(String someName) {
        this.someName = someName;
    }
}
