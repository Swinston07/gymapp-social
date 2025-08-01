package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sterling.Interfaces.WorkoutInviteDAOInterface;
import com.sterling.Models.WorkoutInvite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class WorkoutInviteServiceTest {

    private WorkoutInviteDAOInterface inviteDAO;
    private GymBuddyService gymBuddyService;
    private WorkoutInviteService inviteService;

    @BeforeEach
    public void setUp() {
        inviteDAO = mock(WorkoutInviteDAOInterface.class);
        gymBuddyService = mock(GymBuddyService.class);
        inviteService = new WorkoutInviteService(inviteDAO, gymBuddyService);
    }

    @Test
    void testSendInvite_NoReverseInvite() {
        WorkoutInvite invite = new WorkoutInvite();
        invite.setSenderId(1);
        invite.setRecipientId(2);

        // No reverse invite exists
        when(inviteDAO.findPendingInvite(2, 1)).thenReturn(null);

        String result = inviteService.sendInvite(invite);

        assertEquals("Invite sent successfully!", result);

        verify(inviteDAO, times(1)).findPendingInvite(2, 1);
        verify(inviteDAO, times(1)).saveInvite(invite);
        verifyNoInteractions(gymBuddyService);
    }

    @Test
    void testSendInvite_WithReverseInvite() {
        WorkoutInvite invite = new WorkoutInvite();
        invite.setSenderId(1);
        invite.setRecipientId(2);

        WorkoutInvite reverseInvite = new WorkoutInvite();
        reverseInvite.setSenderId(2);
        reverseInvite.setRecipientId(1);
        reverseInvite.setStatus("pending");

        when(inviteDAO.findPendingInvite(2, 1)).thenReturn(reverseInvite);

        String result = inviteService.sendInvite(invite);

        assertEquals("Invite Accepted!", result);

        verify(inviteDAO, times(1)).findPendingInvite(2, 1);
        verify(inviteDAO, times(1)).updateInviteStatus(reverseInvite);
        verify(inviteDAO, times(1)).saveInvite(invite);
        verify(gymBuddyService, times(1)).addGymBuddy(1, 2);
        verify(gymBuddyService, times(1)).addGymBuddy(2, 1);
    }

    @Test
    void testGetInvitesForUser() {
        WorkoutInvite i1 = new WorkoutInvite();
        WorkoutInvite i2 = new WorkoutInvite();

        when(inviteDAO.getInvitesForUser(1)).thenReturn(List.of(i1, i2));

        List<WorkoutInvite> result = inviteService.getInvitesForUser(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(inviteDAO, times(1)).getInvitesForUser(1);
    }

    @Test
    void testUpdateInviteStatus() {
        Timestamp now = Timestamp.from(Instant.now());

        when(inviteDAO.updateInviteStatus(1, "accepted", now)).thenReturn(true);

        boolean updated = inviteService.updateInviteStatus(1, "accepted", now);

        assertTrue(updated);
        verify(inviteDAO, times(1)).updateInviteStatus(1, "accepted", now);
    }
}
