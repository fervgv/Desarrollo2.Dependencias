package com.example.desarrollo2.di

import com.example.desarrollo2.core.repositories.IUsuarioRepository
import com.example.desarrollo2.core.repositories.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUsuarioRepository(
        usuarioRepository: UsuarioRepository
    ): IUsuarioRepository
}
