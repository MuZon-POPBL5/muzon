package com.muZon.aplicacion.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangeAddressForm {

	@NotNull
	private Long id;

	@NotBlank(message="New Address must not be blank")
	private String newAddress;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((newAddress == null) ? 0 : newAddress.hashCode());
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
		return true;
	}
	@Override
	public String toString() {
		return "ChangePasswordForm [id=" + id +  ", newPassword=" + newAddress
				 + "]";
	}
}

