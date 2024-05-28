package com.example.order.model;

import jakarta.persistence.*;

import java.util.List;



@Entity
public class Order
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
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

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
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

