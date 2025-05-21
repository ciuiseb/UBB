package mpp.repository.interfaces;

import mpp.domain.Participant;

public interface ParticipantRepository extends Repository<Long, Participant> {
    /**
     * Finds a participant by their name.
     *
     * @param name the name of the participant
     * @return the participant with the given name, or null if not found
     */
    Participant findByName(String name);

    /**
     * Finds a participant by their email.
     *
     * @param email the email of the participant
     * @return the participant with the given email, or null if not found
     */
    Participant findByEmail(String email);
}
