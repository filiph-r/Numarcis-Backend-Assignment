package com.example.order.model;

import jakarta.persistence.*;

import java.util.List;



@Entity
@Table(name = "\"order\"")
public class Order
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	@ElementCollection
	private List<Long> productIds;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public List<Long> getProductIds()
	{
		return productIds;
	}

	public void setProductIds(List<Long> productIds)
	{
		this.productIds = productIds;
	}
}

