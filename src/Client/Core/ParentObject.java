package Client.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Алексей on 26.03.2015.
 */
public class ParentObject{
    private ArrayList<SimpleElement> myObject = new ArrayList<SimpleElement>();
    private ObjectSort sortByName = new ObjectSort();
    private ObjectSortByContent sortByContent = new ObjectSortByContent();

    public ArrayList<SimpleElement> getMyObject() {
        return myObject;
    }

    public void addToMyObject(SimpleElement newElement){
        myObject.add(newElement);
    }

    public String aboutMyObject(SimpleElement newElement){
//        return String.format("%td %tB, %tA", newElement.getCurrentDate(), newElement.getCurrentDate(), newElement.getCurrentDate()) + ".\n" + indexOfMyObject(newElement) + ") " + newElement.getTitle() + ": " + newElement.getContent();
        //return String.format("%td %<tB, %<tA. Заметка № %d.\n%td %<tB, %<tA. %s.\n%s", newElement.getCurrentDate(), indexOfMyObject(newElement) + 1, newElement.getCalendar(), newElement.getTitle(), newElement.getContent());
        return String.format("%tc. Заметка № %d.\n%s\n%s", newElement.getCurrentDate(), indexOfMyObject(newElement) + 1, newElement.getTitle(), newElement.getContent());
    }
    public void sortMyObjectByName() {
        Collections.sort(myObject, sortByName);
    }
    public void sortMyObjectByContent() {
        Collections.sort(myObject, sortByContent);
    }
    public void delIndMyObject(int myObject) {
        this.myObject.remove(myObject);
    }
    public boolean delMyObject(SimpleElement myObject) {
        return this.myObject.remove(myObject);
    }
    public void clearMyObject() {
        this.myObject.clear();
    }
    public int sizeMyObject() {
        return this.myObject.size();
    }
    public int indexOfMyObject(SimpleElement myObject) {
        return this.myObject.indexOf(myObject);
    }
    public boolean containsMyObject(String myObject) {
        return this.myObject.contains(myObject);
    }
    public boolean isEmptyMyObject() {
        return this.myObject.isEmpty();
    }
    public SimpleElement getMyObject(int myObject) {
        return this.myObject.get(myObject);
    }

    class ObjectSort implements Comparator <SimpleElement> {
        public int compare (SimpleElement one, SimpleElement two){return one.getTitle().compareTo(two.getTitle());}
    }
    class ObjectSortByContent implements Comparator <SimpleElement> {
        public int compare (SimpleElement one, SimpleElement two){return one.getContent().compareTo(two.getContent());}
    }
}
