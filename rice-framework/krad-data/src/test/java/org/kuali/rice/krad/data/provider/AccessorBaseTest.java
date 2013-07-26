package org.kuali.rice.krad.data.provider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.krad.data.DataObjectWrapper;
import org.kuali.rice.krad.data.DataObjectService;
import org.kuali.rice.krad.data.metadata.DataObjectMetadata;
import org.kuali.rice.krad.data.provider.impl.DataObjectWrapperBase;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.NullValueInNestedPathException;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessorBaseTest {

    @Mock private DataObjectService dataObjectService;
    @Mock private DataObjectMetadata metadata;

    private DataObject dataObject;
    private DataObject2 dataObject2;
    private DataObjectWrapper<DataObject> wrap;

    @Before
    public void setup() throws Exception {
        this.dataObject = new DataObject("1","FieldOne",2);
        this.dataObject2 = new DataObject2("one", "two");
        this.dataObject.setDataObject2(this.dataObject2);
        this.wrap = new DataObjectWrapperImpl<DataObject>(dataObject, metadata, dataObjectService);

        // stub out the metadata for our DataObject type
        when(metadata.getPrimaryKeyAttributeNames()).thenReturn(Collections.singletonList("id"));

        final DataObjectService dataObjectService = this.dataObjectService;
        final DataObjectMetadata metadata = this.metadata;
        // make it so that DataObjectService returns a proper wrap when asked
        when(dataObjectService.wrap(any(DataObject.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                DataObject object = (DataObject)invocation.getArguments()[0];
                return new DataObjectWrapperImpl<DataObject>(object, metadata, dataObjectService);
            }
        });
    }

    static final class DataObjectWrapperImpl<T> extends DataObjectWrapperBase<T> {
        private DataObjectWrapperImpl(T dataObject, DataObjectMetadata metadata, DataObjectService dataObjectService) {
            super(dataObject, metadata, dataObjectService);
        }
    }

    @Test
    public void testGetType() {
        assertEquals(DataObject.class, wrap.getWrappedClass());
    }

    @Test
    public void testGetMetadata() {
        assertEquals(metadata, wrap.getMetadata());
    }

    @Test
    public void testGetWrappedInstance() {
        assertEquals(dataObject, wrap.getWrappedInstance());
    }

    @Test
    public void testGetPrimaryKeyValues() {
        Map<String, Object> primaryKeyValues = wrap.getPrimaryKeyValues();
        assertEquals(1, primaryKeyValues.size());
        assertTrue(primaryKeyValues.containsKey("id"));
        assertEquals("1", primaryKeyValues.get("id"));
    }

    @Test
    public void testEqualsByPrimaryKey() {
        // first check that it's equal to itself
        assertTrue(wrap.equalsByPrimaryKey(dataObject));

        // now create one with an equal primary key but different values for non-pk fields, should be euqual
        assertTrue(wrap.equalsByPrimaryKey(new DataObject("1", "blah", 500)));

        // now create one with a different primary key, should not be equal
        assertFalse(wrap.equalsByPrimaryKey(new DataObject("2", "FieldOne", 2)));

        // let's do some null checking
        assertFalse(wrap.equalsByPrimaryKey(null));

        // verify what happens when primary key is null on object being compared
        assertFalse(wrap.equalsByPrimaryKey(new DataObject(null, null, -1)));

    }

    @Test
    public void testGetPropertyType_Nested() {
        assertEquals(DataObject2.class, wrap.getPropertyType("dataObject2"));
        assertEquals(String.class, wrap.getPropertyType("dataObject2.one"));
        assertEquals(String.class, wrap.getPropertyType("dataObject2.two"));
    }

    @Test
    public void testGetPropertyValueNullSafe() {
        DataObject dataObject = new DataObject("a", "b", 3);
        DataObjectWrapper<DataObject> wrap = new DataObjectWrapperImpl<DataObject>(dataObject, metadata, dataObjectService);
        assertNull(wrap.getPropertyValue("dataObject2"));

        //wrap.setPropertyValue("dataObject2.dataObject3", new DataObject3());

        // assert that a NullValueInNestedPathException is thrown
        try {
            wrap.getPropertyValue("dataObject2.dataObject3");
            fail("NullValueInNestedPathException should have been thrown");
        } catch (NullValueInNestedPathException e) {
            // this should be thrown!
        }

        // now do a null-safe check
        assertNull(wrap.getPropertyValueNullSafe("dataObject2.dataObject3"));

    }


    public static final class DataObject {

        private String id;
        private String fieldOne;
        private Integer fieldTwo;
        private DataObject2 dataObject2;

        DataObject(String id, String fieldOne, Integer fieldTwo) {
            this.id = id;
            this.fieldOne = fieldOne;
            this.fieldTwo = fieldTwo;
            this.dataObject2 = dataObject2;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFieldOne() {
            return fieldOne;
        }

        public void setFieldOne(String fieldOne) {
            this.fieldOne = fieldOne;
        }

        public Integer getFieldTwo() {
            return fieldTwo;
        }

        public void setFieldTwo(Integer fieldTwo) {
            this.fieldTwo = fieldTwo;
        }

        public DataObject2 getDataObject2() {
            return dataObject2;
        }

        public void setDataObject2(DataObject2 dataObject2) {
            this.dataObject2 = dataObject2;
        }
    }

    public static final class DataObject2 {
        private String one;
        private String two;
        private DataObject3 dataObject3;

        public DataObject2() {}

        public DataObject2(String one, String two) {
            this.one = one;
            this.two = two;
        }

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }

        public DataObject3 getDataObject3() {
            return dataObject3;
        }

        public void setDataObject3(DataObject3 dataObject3) {
            this.dataObject3 = dataObject3;
        }
    }

    public static final class DataObject3 {

        private String hello;
        private String world;

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public String getWorld() {
            return world;
        }

        public void setWorld(String world) {
            this.world = world;
        }
    }

}
