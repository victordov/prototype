package com.epam.customer.converter.populator;

import com.epam.customer.BaseTest;
import com.epam.customer.data.EpamCustomerData;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.user.CustomerModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@UnitTest
public class EpamCustomerPopulatorTest extends BaseTest {

    @Mock
    private CustomerNameStrategy mockCustomerNameStrategy;

    private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"); // See epamcore-spring.xml
    private EpamCustomerPopulator epamCustomerPopulator;
    private CustomerModel source;
    private EpamCustomerData target;

    @Before
    public void setUp() {
        epamCustomerPopulator = new EpamCustomerPopulator(mockCustomerNameStrategy, dateFormatter);
        source = getCustomerModel();
        target = new EpamCustomerData();
    }

    private CustomerModel getCustomerModel() {
        CustomerModel source = new CustomerModel();
        source.setCreationtime(new Date());
        return source;
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParameterSourceIsNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter source cannot be null.");

        epamCustomerPopulator.populate(null, new EpamCustomerData());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenParameterTargetIsNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter target cannot be null.");

        epamCustomerPopulator.populate(new CustomerModel(), null);
    }

    @Test
    public void shouldSetUidWhenSourceUidIsNotNull() {
        String expectedUid = "Uid";
        source.setUid(expectedUid);

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Uid should be equals <%s>.", expectedUid);
        assertEquals(errorMsg, expectedUid, target.getUid());
    }

    @Test
    public void shouldNotSetUidWhenSourceUidIsNull() {
        epamCustomerPopulator.populate(source, target);

        assertNull("Uid should be null.", target.getUid());
    }

    @Test
    public void shouldSetNameWhenSourceNameIsNotNull() {
        String expectedName = "John";
        source.setName(expectedName);

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Name should be equals <%s>.", expectedName);
        assertEquals(errorMsg, expectedName, target.getName());
    }

    @Test
    public void shouldNotSetNameWhenSourceNameIsNull() {
        epamCustomerPopulator.populate(source, target);

        assertNull("Name should be null.", target.getName());
    }

    @Test
    public void shouldSetNullsIntoEpamCustomerDataFirstNameAndLastNameWhenCustomerNameIsNull() {
        CustomerModel source = new CustomerModel();
        source.setCreationtime(new Date());
        EpamCustomerData target = new EpamCustomerData();
        when(mockCustomerNameStrategy.splitName(anyString())).thenReturn(null);

        epamCustomerPopulator.populate(source, target);

        assertNull("FirstName should be null", target.getFirstName());
        assertNull("LastName should be null", target.getLastName());
    }

    @Test
    public void shouldSetEpamCustomerDataFirstNameAndLastName() {
        CustomerModel source = new CustomerModel();
        source.setCreationtime(new Date());
        EpamCustomerData target = new EpamCustomerData();
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String[] names = {expectedFirstName, expectedLastName};
        when(mockCustomerNameStrategy.splitName(anyString())).thenReturn(names);

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("FirstName should be <%s>.", expectedFirstName);
        assertEquals(errorMsg, expectedFirstName, target.getFirstName());
        errorMsg = String.format("LastName should be <%s>.", expectedLastName);
        assertEquals(errorMsg, expectedLastName, target.getLastName());
    }

    @Test
    public void shouldSetEmailWhenSourceUidIsNotNull() {
        String expectedEmail = "john_dou@gmail.com";
        source.setUid(expectedEmail);

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Email should be equals <%s>.", expectedEmail);
        assertEquals(errorMsg, expectedEmail, target.getEmail());
    }

    @Test
    public void shouldNotSetEmailWhenSourceUidIsNull() {
        epamCustomerPopulator.populate(source, target);

        assertNull("Email should be null.", target.getEmail());
    }

    @Test
    public void shouldSetActiveFalseWhenSourcesIsLoginDisabledIsTrue() {
        boolean isLoginDisabled = true;
        source.setLoginDisabled(isLoginDisabled);
        boolean expectedActive = !isLoginDisabled;

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Active should be equals <%b>.", expectedActive);
        assertFalse(errorMsg, target.isActive());
    }

    @Test
    public void shouldSetActiveTrueWhenSourcesIsLoginDisabledIsFalse() {
        boolean isLoginDisabled = false;
        source.setLoginDisabled(isLoginDisabled);
        boolean expectedActive = !isLoginDisabled;

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Active should be equals <%b>.", expectedActive);
        assertTrue(errorMsg, target.isActive());
    }

    @Test
    public void shouldSetCreatedDate() {
        source = new CustomerModel();
        Date currentDate = new Date();
        String expectedCreatedDate = dateFormatter.format(currentDate);
        source.setCreationtime(currentDate);

        epamCustomerPopulator.populate(source, target);

        String errorMsg = String.format("Active should be equals <%s>.", expectedCreatedDate);
        assertEquals(errorMsg, expectedCreatedDate, target.getCreatedDate());
    }

}