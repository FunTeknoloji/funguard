import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

export const HomeScreen = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>FunGuard Cyber</Text>
      <View style={styles.shield} />
      <Text style={styles.status}>SİSTEM AKTİF</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#000', alignItems: 'center', justifyContent: 'center' },
  title: { color: '#B026FF', fontSize: 32, fontWeight: 'bold' },
  shield: { width: 200, height: 200, borderRadius: 100, borderWidth: 4, borderColor: '#00FFFF', marginTop: 40 },
  status: { color: '#FFF', fontSize: 24, marginTop: 20 }
});
