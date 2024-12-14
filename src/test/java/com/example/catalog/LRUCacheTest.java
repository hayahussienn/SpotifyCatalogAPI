package com.example.catalog;

import com.example.catalog.utils.LRUCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LRUCacheTest {

    LRUCache<String, String> cache;

    @Nested
    @DisplayName("when instantiated with capacity 3")
    class WhenInstantiated {

        @BeforeEach
        void createNewCache() {
            cache = new LRUCache<>(3);
        }

        @Test
        @DisplayName("cache is initially empty")
        void isEmpty() {
            assertEquals(0, cache.size(), "Cache should be empty initially");
        }

        @Test
        @DisplayName("throws NullPointerException when getting a null key")
        void throwsExceptionWhenGettingNullKey() {
            assertThrows(NullPointerException.class, () -> cache.get(null), "Should throw exception when key is null");
        }

        @Test
        @DisplayName("throws NullPointerException when setting a null key")
        void throwsExceptionWhenSettingNullKey() {
            assertThrows(NullPointerException.class, () -> cache.set(null, "value"), "Should throw exception when key is null");
        }

        @Nested
        @DisplayName("after adding 2 elements")
        class AfterAdding2Elements {

            @BeforeEach
            void addElements() {
                cache.set("haya", "hussien");
                cache.set("noor", "huss");
            }

            @Test
            @DisplayName("cache contains the added elements")
            void containsAddedElements() {
                assertEquals(2, cache.size(), "Cache should contain 2 elements");
                assertEquals("hussien", cache.get("haya"), "Value for key 'haya' should be 'hussien'");
                assertEquals("huss", cache.get("noor"), "Value for key 'noor' should be 'huss'");
            }

            @Test
            @DisplayName("cache maintains the same size and content after adding duplicate keys")
            void maintainsSizeAndContentAfterDuplicateKeys() {
                cache.set("haya", "updated");
                assertEquals(2, cache.size(), "Cache size should remain 2 after updating a key");
                assertEquals("updated", cache.get("haya"), "Value for key 'haya' should be updated to 'updated'");
                assertEquals("huss", cache.get("noor"), "Value for key 'noor' should remain 'huss'");
            }

            @Test
            @DisplayName("can add one more element without evicting existing keys")
            void canAddOneMoreElementWithoutEviction() {
                cache.set("newKey", "newValue");
                assertEquals(3, cache.size(), "Cache size should be 3 after adding a third element");
                assertEquals("hussien", cache.get("haya"), "Value for key 'haya' should remain 'hussien'");
                assertEquals("huss", cache.get("noor"), "Value for key 'noor' should remain 'huss'");
                assertEquals("newValue", cache.get("newKey"), "Value for key 'newKey' should be 'newValue'");
            }

            @Test
            @DisplayName("updates the value of an existing key when set again")
            void updatedValues() {
                cache.set("haya", "updatedValue");
                assertEquals("updatedValue", cache.get("haya"));
            }


            @Test
            @DisplayName("maintains correct order of elements after accessing an existing element")
            void maintainsCorrectOrderAfterAccess() {
                cache.get("haya"); // Access 'haya'
                cache.set("newKey", "newValue"); // Add a new element
                assertEquals(3, cache.size(), "Cache size should be 3 after adding a third element");
                assertEquals("hussien", cache.get("haya"), "Value for key 'haya' should remain 'hussien'");
                assertEquals("huss", cache.get("noor"), "Value for key 'noor' should remain 'huss'");
                assertEquals("newValue", cache.get("newKey"), "Value for key 'newKey' should be 'newValue'");
            }
        }

        @Nested
        @DisplayName("after adding 3 elements")
        class AfterAdding3Elements {

            @BeforeEach
            void addElements() {
                cache.set("A1", "B1");
                cache.set("A2", "B2");
                cache.set("A3", "B3");
            }


            @Test
            @DisplayName("size does not exceed capacity after adding more elements")
            void doesNotExceedCapacity() {
                cache.set("A4", "B4"); // Add one more element
                assertEquals(3, cache.size(), "Cache size should not exceed capacity of 3");
                assertEquals(null, cache.get("A1"), "Key 'A1' should be evicted as it was least recently used");
                assertEquals("B2", cache.get("A2"), "Key 'A2' should still be present");
                assertEquals("B3", cache.get("A3"), "Key 'A3' should still be present");
                assertEquals("B4", cache.get("A4"), "Key 'A4' should be added");
            }

            @Test
            @DisplayName("evicts the least recently used element when the cache exceeds its capacity")
            void evictsTheLeastUsedElement() {
                cache.set("A4", "B4"); // Add one more element
                assertEquals(null, cache.get("A1"), "Key 'A1' should be evicted as it was the least recently used");
                assertEquals("B2", cache.get("A2"), "Key 'A2' should still be present");
                assertEquals("B3", cache.get("A3"), "Key 'A3' should still be present");
                assertEquals("B4", cache.get("A4"), "Key 'A4' should be added to the cache");
            }


            @Nested
            @DisplayName("when cleared")
            class WhenCleared {

                @BeforeEach
                void clearCache() {
                    cache.clear();
                }

                @Test
                @DisplayName("cache is empty after clear")
                void isEmptyAfterClear() {
                    assertEquals(0, cache.size(), "Cache should be empty after clear");
                }

                @Test
                @DisplayName("added elements are not accesible")
                void notAccesibleElements() {
                    assertEquals(null, cache.get("A1"), "Key 'A1' should be null");
                    assertEquals(null, cache.get("A2"), "Key 'A2' should be null");
                    assertEquals(null, cache.get("A3"), "Key 'A3' should be null");

                }
            }
        }
    }
}
