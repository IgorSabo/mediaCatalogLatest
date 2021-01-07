package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Igor on 8/19/2020.
 */
@Entity
@Table(name = "mediaHost")
@Getter
@Setter
public class MediaHost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;

    @OneToMany(mappedBy = "mediaHost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyJoinColumn(name = "mediaHostId")
    private Set<MediaFolder> folders;

    private String name;

    private boolean remote = false;

}
