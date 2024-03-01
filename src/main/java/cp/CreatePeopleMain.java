package cp;

import cp.entity.City;
import cp.entity.People;
import jakarta.persistence.*;
import java.util.*;

public class CreatePeopleMain {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        TypedQuery<String> cityTypedQuery = manager.createQuery(
                "select c.name from City c", String.class
        );
        List<String> cityList = cityTypedQuery.getResultList();

        Scanner scan = new Scanner(System.in);

        System.out.println("Select city of residence from this list (enter a number):");
        int i = 1;
        for (String name : cityList) {
            System.out.printf("%d. %s\n", i, name);
            i++;
        }
        int cityId = scan.nextInt();
        scan.nextLine();

        System.out.print("Enter your name: ");
        String personName = scan.nextLine();

        scan.close();

        City city = manager.find(City.class, cityId);

        try {
            manager.getTransaction().begin();

            People person = new People();
            person.setName(personName);
            person.setCity(city);

            manager.persist(person);
            manager.getTransaction().commit();

        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        manager.close();
        factory.close();
    }
}