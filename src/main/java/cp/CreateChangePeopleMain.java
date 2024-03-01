package cp;

import cp.entity.City;
import cp.entity.People;
import jakarta.persistence.*;

import java.util.List;
import java.util.Scanner;

public class CreateChangePeopleMain {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String personName = scan.nextLine();

        TypedQuery<Long> peopleQuery = manager.createQuery(
                "select count(p.id) from People p where p.name = ?1", Long.class
        );
        peopleQuery.setParameter(1, personName);
        long personCount = peopleQuery.getSingleResult();

        if (personCount > 0) {
            System.out.printf("Name '%s' is already exists, enter new name to change it: ", personName);
            String newName = scan.nextLine();
            System.out.println("");

            TypedQuery<String> cityTypedQuery = manager.createQuery(
                    "select c.name from City c", String.class
            );
            List<String> cityList = cityTypedQuery.getResultList();
            System.out.printf("Select city of residence for '%s' (enter a number):\n", newName);

            int i = 1;
            for (String name : cityList) {
                System.out.printf("%d. %s\n", i, name);
                i++;
            }
            int cityId = scan.nextInt();
            City city = manager.find(City.class, cityId);

            try {
                manager.getTransaction().begin();

                Query changeName = manager.createQuery(
                        "update People p set p.name = ?1 where p.name = ?2"
                );
                changeName.setParameter(1, newName);
                changeName.setParameter(2, personName);
                changeName.executeUpdate();

                Query changeCity = manager.createQuery(
                        "update People p set p.city = ?1 where p.name = ?2"
                );
                changeCity.setParameter(1, city);
                changeCity.setParameter(2, newName);
                changeCity.executeUpdate();

                manager.getTransaction().commit();

            } catch (Exception e) {
                manager.getTransaction().rollback();
                throw new RuntimeException(e);
            }

        } else {
            TypedQuery<String> cityTypedQuery = manager.createQuery(
                    "select c.name from City c", String.class
            );
            List<String> cityList = cityTypedQuery.getResultList();
            System.out.printf("Select city of residence for '%s' (enter a number):\n", personName);

            int i = 1;
            for (String name : cityList) {
                System.out.printf("%d. %s\n", i, name);
                i++;
            }
            int cityId = scan.nextInt();
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
        }
        manager.close();
        factory.close();
    }
}
