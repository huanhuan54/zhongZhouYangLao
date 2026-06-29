package com.ruoyi.nursing.domain;

import java.io.Serializable;

/**
 * Caregiver option.
 */
public class Caregiver implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String phone;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
