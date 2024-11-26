package ubb.scs.map.domain.network;

import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.domain.graph.UndirectedGraph;

import java.util.ArrayList;
import java.util.Comparator;

public class CommunityNetwork extends UndirectedGraph<Long> {
    /**
     * @return the number of communities
     */
    public int getNumberOfCommunities() {
        return getCommunities().size();
    }
    /**
     * @return the communities
     */
    public ArrayList<ArrayList<Long>> getCommunities() {
        return super.getConnexComponents();
    }
    /**
     * @return the most social group
     */
    public ArrayList<Long> getMostSocialGroup() {
        return getCommunities()
                .stream()
                .max(Comparator.comparing(ArrayList::size))
                .orElseThrow(() -> new DomainException("There are no communities yet!"));
    }
}