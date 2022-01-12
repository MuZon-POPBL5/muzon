package com.muZon.aplicacion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangeAddressForm {

	@NotNull
	private Long id;

	@NotBlank(message="Address must not be blank")
	private String newAddress;

	@NotBlank(message="Country must not be blank")
	private String newCountry;

	@NotBlank(message="Zip Code must not be blank")
	private int newZipCode;

	@NotBlank(message="City must not be blank")
	private String newCity;

	public ChangeAddressForm() { }
	public ChangeAddressForm(Long id) {this.id = id;}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNewAddress() {
		return newAddress;
	}
	public void setNewAddress(String newAddress) {
		this.newAddress = newAddress;
	}

	public String getNewCity() {
		return newCity;
	}
	public void setNewCity(String newCity) {
		this.newCity = newCity;
	}

	public int getNewZipCode() {
		return newZipCode;
	}
	public void setNewZipCode(int newZipCode) {
		this.newZipCode = newZipCode;
	}

	public String getNewCountry() {
		return newCountry;
	}
	public void setNewCountry(String newCountry) {
		this.newCountry = newCountry;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((newAddress == null) ? 0 : newAddress.hashCode());
		result = prime * result + ((newCity == null) ? 0 : newCity.hashCode());
		result = prime * result + ((newCountry == null) ? 0 : newCountry.hashCode());
		result = prime * result + ((newZipCode == 0) ? 0 : newZipCode);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeAddressForm other = (ChangeAddressForm) obj;
		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (newAddress == null) {
			if (other.newAddress != null)
				return false;
		} else if (!newAddress.equals(other.newAddress))
			return false;
		if (newCity == null) {
			if (other.newCity != null)
				return false;
		} else if (!newCity.equals(other.newCity))
			return false;
		if (newCountry == null) {
			if (other.newCountry != null)
				return false;
		} else if (!newCountry.equals(other.newCountry))
			return false;
		if (newZipCode == -1) {
			if (other.newZipCode != -1)
				return false;
		} else if (newZipCode != other.newZipCode)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ChangePasswordForm [id=" + id +  ", newPassword=" + newAddress +  ", newCity=" + newCity
				+  ", newCountry=" + newCountry  + ", newZipCode=" + newZipCode + "]";
	}
}