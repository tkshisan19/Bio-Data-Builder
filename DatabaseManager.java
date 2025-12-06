import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManager {
    private List<BioData> savedBioData;
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        this.savedBioData = new ArrayList<>();
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public void saveBioData(BioData bioData) {
        savedBioData.add(bioData);
    }
    
    public List<BioData> getAllBioData() {
        return new ArrayList<>(savedBioData);
    }
    
    public List<BioData> getBioDataByType(String type) {
        return savedBioData.stream()
            .filter(bd -> bd.getBioDataType().equals(type))
            .collect(Collectors.toList());
    }
    
    public int getCount() {
        return savedBioData.size();
    }
    
    public void clear() {
        savedBioData.clear();
    }
}
