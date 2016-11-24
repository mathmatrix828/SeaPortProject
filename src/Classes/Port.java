package Classes;

import Base.*;
import Classes.Ships.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Port extends BaseObject {

    private HashMap<Integer, Dock> docks = new HashMap<Integer, Dock>();
    private HashMap<Integer, Ship> queue = new HashMap<Integer, Ship>();
    private HashMap<Integer, Person> persons = new HashMap<Integer, Person>();

    public Port() {
        super();
    }

    public Port(String n) {
        // Port has no parent (except world), so we only have to call BaseObject(String)
        super(n);
    }

    @Override
    public String toString() {
        String rtr = "";

        rtr += String.format("%s\n", name);
        for(Map.Entry<Integer, Dock> dockEntry: docks.entrySet()) {
            Integer i = dockEntry.getKey();
            Dock d = dockEntry.getValue();

            rtr += String.format("(%d) Dock: %s\n", i, d);
        }

        rtr += "Port Queue\n";
        for(Map.Entry<Integer, Ship> shipEntry: queue.entrySet()) {
            Integer i = shipEntry.getKey();
            Ship s = shipEntry.getValue();

            rtr += String.format("\t(%d) Ship: %s\n", i, s);
        }
        rtr += "Personnel Assigned\n";
        for(Map.Entry<Integer, Person> personEntry: persons.entrySet()) {
            Integer i = personEntry.getKey();
            Person p = personEntry.getValue();

            rtr += String.format("\t(%d) Person: %s\n", i, p);
        }

        rtr += "\n\n"; // Pushing the next string down
        return rtr;
    }

    @Override
    public DefaultMutableTreeNode getTree(Integer i) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(String.format("%d: %s", i, name));

        DefaultMutableTreeNode dockNode = new DefaultMutableTreeNode("Docks");
        for(Map.Entry<Integer, Dock> dockEntry: docks.entrySet()) {
            dockNode.add(dockEntry.getValue().getTree(dockEntry.getKey()));
        }
        rootNode.add(dockNode);

        DefaultMutableTreeNode queueNode = new DefaultMutableTreeNode("Port Queue");
        for(Map.Entry<Integer, Ship> shipEntry: queue.entrySet()) {
            queueNode.add(shipEntry.getValue().getTree(shipEntry.getKey()));
        }
        rootNode.add(queueNode);

        DefaultMutableTreeNode personnelNode = new DefaultMutableTreeNode("Personnel Assigned");
        for(Map.Entry<Integer, Person> personEntry: persons.entrySet()) {
            personnelNode.add(personEntry.getValue().getTree(personEntry.getKey()));
        }
        rootNode.add(personnelNode);

        return rootNode;
    }

    public void startJobs() {
        for(Map.Entry<Integer, Dock> dockEntry: docks.entrySet()) {
            dockEntry.getValue().startJobs();
        }
    }

    // Public adding methods
    public void addDock(Integer k, Dock d) {
        docks.put(k, d);
    }
    public void addQueue(Integer k, Ship s) { queue.put(k, s); }
    public void addPerson(Integer k, Person p) {persons.put(k, p); }


    // Search methods
    public Dock findDock(Integer i) {
        if(docks.containsKey(i)) {
            return docks.get(i);
        }
        return null;
    }
    public Ship findShip(Integer i) {
        if(queue.containsKey(i)) {
            return queue.get(i);
        }

        for(Map.Entry<Integer, Dock> dockEntry: docks.entrySet()) {
            Dock d = dockEntry.getValue();
            Integer id = d.getShipID();
            if (Objects.equals(i, id)) {
                return d.getDockedShip();
            }
        }
        return null;
    }
    public Job findJob(Integer i) {
        for(Map.Entry<Integer, Ship> shipEntry: queue.entrySet()) {
            Job j = shipEntry.getValue().findJob(i);
        }

        for(Map.Entry<Integer, Dock> dockEntry: docks.entrySet()) {
            Job j = dockEntry.getValue().getDockedShip().findJob(i);
            if(j != null) { return j; }
        }
        return null;
    }
    public Person findPerson(Integer i) {
        if(persons.containsKey(i)) {
            return persons.get(i);
        }
        return null;
    }
}