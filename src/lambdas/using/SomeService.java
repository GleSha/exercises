package lambdas.using;

import lambdas.SomeBusinessLogicEntity;

import java.util.*;

/**
 * Some service class that works with SomeBusinessLogicEntity, there will be many classes like this in application
 */
public class SomeService {

    public List<SomeBusinessLogicEntity> getEntitiesSortedFirst() {
        List<SomeBusinessLogicEntity> entitiesFromSomeStorage = getEntitiesFromSomeStorage();
        // in this case code looks more obvious (if you have a little experience)
        entitiesFromSomeStorage.sort((o1, o2) -> Integer.compare(o1.getSomeParameter(), o2.getSomeParameter()));
        return entitiesFromSomeStorage;
    }

    public List<SomeBusinessLogicEntity> getEntitiesSortedSecond() {
        List<SomeBusinessLogicEntity> entitiesFromSomeStorage = getEntitiesFromSomeStorage();
        // it took us 3 less lines of code, it looks more obvious
        // 3 lines of code in one lambda call can save more lines, if there are many such calls
        entitiesFromSomeStorage.sort((o1, o2) -> {
            if (o1.getSomeName().equals(o2.getSomeName())) {
                return Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
            } else if (o1.getSomeName().length() < o2.getSomeName().length()) {
                return (-1) * Integer.compare(o1.getSomeProperties().size(), o2.getSomeProperties().size());
            } else if (o1.getSomeName().length() == o2.getSomeName().length()) {
                return (-1) * Integer.compare(o1.getSomeParameter(), o2.getSomeProperties().size());
            } else {
                return Integer.compare(o1.getSomeParameter(), o2.getSomeParameter());
            }
        });
        return entitiesFromSomeStorage;
    }



    private List<SomeBusinessLogicEntity> getEntitiesFromSomeStorage() {
        return new ArrayList<>() {{
            add(new SomeBusinessLogicEntity(1, Arrays.asList(1, 2, 3), "2"));
            add(new SomeBusinessLogicEntity(2, Arrays.asList(1, 3, 4), "asdasd"));
            add(new SomeBusinessLogicEntity(3, Arrays.asList(5, 86, 3), "oiuy"));
            add(new SomeBusinessLogicEntity(2, Arrays.asList(87, -34, 0), "123hjkl"));
            add(new SomeBusinessLogicEntity(6, Arrays.asList(1123, 76, -2), "pov64bvof"));
            add(new SomeBusinessLogicEntity(10, Arrays.asList(123, 9548, 34),"mcod75jf"));
        }};
    }
}
