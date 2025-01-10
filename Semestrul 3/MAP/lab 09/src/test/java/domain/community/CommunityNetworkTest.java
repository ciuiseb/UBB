package domain.community;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.network.CommunityNetwork;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CommunityNetworkTest {

    private CommunityNetwork communityNetwork;

    @BeforeEach
    public void setUp() {
        communityNetwork = new CommunityNetwork();
    }

    // Test the initial number of communities
    @Test
    public void getNumberOfCommunitiesInitialTest() {
        assertEquals(0, communityNetwork.getNumberOfCommunities(), "Initial number of communities should be 0");
    }

    // Test adding nodes and edges to form communities
    @Test
    public void addEdgesAndGetNumberOfCommunitiesTest() {
        communityNetwork.addIsolatedNode(1L);
        communityNetwork.addIsolatedNode(2L);
        communityNetwork.addIsolatedNode(3L);
        communityNetwork.addEdge(1L, 2L);

        assertEquals(2, communityNetwork.getNumberOfCommunities(), "There should be 2 communities after adding edges");
    }

    // Test getting communities
    @Test
    public void getCommunitiesTest() {
        communityNetwork.addIsolatedNode(1L);
        communityNetwork.addIsolatedNode(2L);
        communityNetwork.addEdge(1L, 2L);
        communityNetwork.addIsolatedNode(3L);

        ArrayList<ArrayList<Long>> communities = communityNetwork.getCommunities();
        assertEquals(2, communities.size(), "There should be 2 communities");
        assertTrue(communities.get(0).contains(1L) && communities.get(0).contains(2L), "First community should contain 1 and 2");
        assertTrue(communities.get(1).contains(3L), "Second community should contain 3");
    }

    // Test getting the most social group
    @Test
    public void getMostSocialGroupTest() {
        communityNetwork.addIsolatedNode(1L);
        communityNetwork.addIsolatedNode(2L);
        communityNetwork.addIsolatedNode(3L);
        communityNetwork.addEdge(1L, 2L);
        communityNetwork.addEdge(1L, 3L); // 1 is now connected to 2 and 3

        ArrayList<Long> mostSocialGroup = communityNetwork.getMostSocialGroup();
        assertEquals(3, mostSocialGroup.size(), "The most social group should contain 1, 2, and 3");
        assertTrue(mostSocialGroup.contains(1L));
        assertTrue(mostSocialGroup.contains(2L));
        assertTrue(mostSocialGroup.contains(3L));
    }

    // Test getting most social group when there are no communities
    @Test
    public void getMostSocialGroupNoCommunitiesTest() {
        Exception exception = assertThrows(DomainException.class, () -> {
            communityNetwork.getMostSocialGroup();
        });
        assertEquals("There are no communities yet!", exception.getMessage());
    }

    @AfterEach
    public void tearDown() {
        communityNetwork = null; // Clean up after each test
    }
}
