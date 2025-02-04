package com.example.catalog;

import com.example.catalog.utils.SpotifyUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.example.catalog.utils.SpotifyUtils.isValidId;
import static org.junit.jupiter.api.Assertions.*;


public class SpotifyUtilsTest {

    @Test
    public void testValidId() {
        assertTrue(isValidId("6rqhFgbbKwnb9MLmUQDhG6")); // valid Spotify ID
        assertTrue(isValidId("1a2B3c4D5e6F7g8H9iJkL0mN")); // valid 22 character ID
        assertTrue(isValidId("a1b2C3d4E5f6G7h8I9jK0L1m2N")); // valid 30 character ID
    }


    @Test
    public void testValidURI() {
        assertTrue(SpotifyUtils.isValidURI("spotify:track:4iV5W9uYEdYUVa79Axb7Rh")); // valid URI
        assertTrue(SpotifyUtils.isValidURI("spotify:album:1DFixLWuPkv3KT3TnV35m3")); // valid album URI
        assertTrue(SpotifyUtils.isValidURI("spotify:artist:6rqhFgbbKwnb9MLmUQDhG6")); // valid artist URI
        assertTrue(SpotifyUtils.isValidURI("spotify:playlist:37i9dQZF1DXcBWIGoYBM5M")); // valid playlist URI
    }

    @Test
    public void testInvalidURI() {
        assertFalse(SpotifyUtils.isValidURI(null)); // null URI
        assertFalse(SpotifyUtils.isValidURI("")); // empty string
        assertFalse(SpotifyUtils.isValidURI("spotify:track:")); // missing ID
        assertFalse(SpotifyUtils.isValidURI("spotify:invalid:4iV5W9uYEdYUVa79Axb7Rh")); // invalid resource type
        assertFalse(SpotifyUtils.isValidURI("http://example.com")); // invalid format
        assertFalse(SpotifyUtils.isValidURI("spotify:track:invalidID")); // invalid ID
    }

    @Test
    public void testGetSpotifyClient_InvalidInputs() {
        // Test null client ID
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient(null, "validSecret");
        }, "Should throw exception when client ID is null");

        // Test empty client ID
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("", "validSecret");
        }, "Should throw exception when client ID is empty");

        // Test null client secret
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("validId", null);
        }, "Should throw exception when client secret is null");

        // Test empty client secret
        assertThrows(IllegalArgumentException.class, () -> {
            SpotifyUtils.getSpotifyClient("validId", "");
        }, "Should throw exception when client secret is empty");

        // Test valid input (should throw UnsupportedOperationException)
        assertThrows(UnsupportedOperationException.class, () -> {
            SpotifyUtils.getSpotifyClient("validClientId", "validClientSecret");
        }, "Should throw UnsupportedOperationException for valid input");
    }





}


