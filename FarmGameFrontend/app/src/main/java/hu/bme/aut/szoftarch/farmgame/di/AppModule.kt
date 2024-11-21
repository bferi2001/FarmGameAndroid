package hu.bme.aut.szoftarch.farmgame.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.szoftarch.farmgame.api.LoginHandler
import hu.bme.aut.szoftarch.farmgame.api.LoginHandlerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule{
    @Provides
    @Singleton
    fun bindLoginHandler(): LoginHandler{
        return LoginHandlerImpl()
    }
}