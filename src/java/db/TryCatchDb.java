package db;

import java.util.List;

/**
 *
 * @author ryouhei
 */
public class TryCatchDb<T> extends SuperDb {
    public TryCatchDb(Class entityClass) {
        super(entityClass);
    }
    
    public void add(T obj){
        try{
            create(obj);
        }catch(Exception e){}
    }
    
    public void update(T obj){
        try{
            edit(obj);
        }catch(Exception e){}
    }
    
    public void delete(T obj){
        try{
            remove(obj);
        }catch(Exception e){}
    }
    
    public T search(Object id){
        return (T)find(id);
    }

    public List<T> getAll() {
        return findAll();
    }
    
    public List<T> getRange(int[] range) {
        return findRange(range);
    }

    public int dataCount() {
        return count();
    }
}
