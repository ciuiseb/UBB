package ubb.scs.map.domain.network;

import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.domain.graph.UndirectedGraph;

import java.util.ArrayList;

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
        if (getCommunities().isEmpty()) {
            throw new DomainException("There are no communities yet!");
        }

        int biggestCommunitySize = 0;
        ArrayList<Long> biggestCommunity = new ArrayList<>();
        for (var community : getCommunities()) {
            if (community.size() > biggestCommunitySize) {
                biggestCommunity.clear();
                biggestCommunity.addAll(community);
                biggestCommunitySize = biggestCommunity.size();
            }
        }
        return biggestCommunity;
    }

}